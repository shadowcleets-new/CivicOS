// [MODULE] Image Utilities
// [PURPOSE] Image processing for privacy and security compliance
// [COMPLIANCE] Strip EXIF metadata, compress images, anonymize photos

package com.nivar.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * [CLASS] ImageUtils - Image processing utilities
 * [PURPOSE] Privacy-compliant image handling
 * [FEATURES] EXIF stripping, compression, anonymization
 */
object ImageUtils {
    
    /**
     * [METHOD] Strip EXIF metadata from image
     * [PURPOSE] Remove GPS, timestamp, device info from photos
     * [PRIVACY] Prevents de-anonymization through image metadata
     * [PARAMS] imageUri - URI of image to process
     * [PARAMS] context - Application context
     * [RETURNS] URI of processed image with EXIF data removed
     * [COMPLIANCE] Required for anonymous mode
     */
    fun stripExifData(imageUri: Uri, context: Context): Uri {
        try {
            // [INPUT STREAM] Open image file for reading
            val inputStream = context.contentResolver.openInputStream(imageUri)
                ?: return imageUri  // [ERROR] Return original if can't open
            
            // [EXIF] Read EXIF data from image
            val exif = ExifInterface(inputStream)
            
            // [REMOVE GPS] Strip GPS coordinates
            // [PRIVACY] Prevents location tracking through photo metadata
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, null)
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, null)
            exif.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, null)
            exif.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, null)
            exif.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, null)
            
            // [REMOVE TIMESTAMP] Strip capture date/time
            // [PRIVACY] Prevents temporal correlation
            exif.setAttribute(ExifInterface.TAG_DATETIME, null)
            exif.setAttribute(ExifInterface.TAG_DATETIME_ORIGINAL, null)
            exif.setAttribute(ExifInterface.TAG_DATETIME_DIGITIZED, null)
            
            // [REMOVE DEVICE INFO] Strip camera make/model
            // [PRIVACY] Prevents device fingerprinting
            exif.setAttribute(ExifInterface.TAG_MAKE, null)
            exif.setAttribute(ExifInterface.TAG_MODEL, null)
            exif.setAttribute(ExifInterface.TAG_SOFTWARE, null)
            
            // [REMOVE USER INFO] Strip author/copyright
            exif.setAttribute(ExifInterface.TAG_ARTIST, null)
            exif.setAttribute(ExifInterface.TAG_COPYRIGHT, null)
            
            // [SAVE] Write modified EXIF data back to file
            exif.saveAttributes()
            inputStream.close()
            
            return imageUri  // [RETURN] URI of cleaned image
            
        } catch (e: IOException) {
            // [ERROR] Log error and return original image
            android.util.Log.e("ImageUtils", "Error stripping EXIF data", e)
            return imageUri
        }
    }
    
    /**
     * [METHOD] Compress image to reduce file size
     * [PURPOSE] Reduce upload bandwidth and storage costs
     * [PARAMS] imageUri - URI of image to compress
     * [PARAMS] context - Application context
     * [PARAMS] maxSizeKB - Maximum file size in KB (default 1024 = 1MB)
     * [PARAMS] quality - JPEG quality 0-100 (default 85)
     * [RETURNS] URI of compressed image
     * [PERFORMANCE] Reduces upload time and backend storage
     */
    fun compressImage(
        imageUri: Uri,
        context: Context,
        maxSizeKB: Int = 1024,
        quality: Int = 85
    ): Uri {
        try {
            // [DECODE] Load image as bitmap
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            // [RESIZE] Scale down if too large
            val resizedBitmap = if (bitmap.width > 1920 || bitmap.height > 1920) {
                // [MAX DIMENSION] Limit to 1920px (Full HD)
                val scale = minOf(1920f / bitmap.width, 1920f / bitmap.height)
                val matrix = Matrix().apply { postScale(scale, scale) }
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }
            
            // [OUTPUT FILE] Create temporary file for compressed image
            val outputFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(outputFile)
            
            // [COMPRESS] Save as JPEG with specified quality
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.close()
            
            // [CLEANUP] Recycle bitmaps to free memory
            bitmap.recycle()
            if (resizedBitmap != bitmap) {
                resizedBitmap.recycle()
            }
            
            return Uri.fromFile(outputFile)  // [RETURN] URI of compressed image
            
        } catch (e: Exception) {
            android.util.Log.e("ImageUtils", "Error compressing image", e)
            return imageUri  // [ERROR] Return original on failure
        }
    }
    
    /**
     * [METHOD] Process image for anonymous submission
     * [PURPOSE] Strip EXIF and compress in one operation
     * [PARAMS] imageUri - URI of image to process
     * [PARAMS] context - Application context
     * [RETURNS] URI of anonymized and compressed image
     * [USE CASE] Anonymous mode grievance submissions
     */
    fun anonymizeImage(imageUri: Uri, context: Context): Uri {
        // [STEP 1] Strip EXIF metadata
        val cleanedUri = stripExifData(imageUri, context)
        
        // [STEP 2] Compress to reduce file size
        val compressedUri = compressImage(cleanedUri, context)
        
        return compressedUri
    }
    
    /**
     * [METHOD] Get image file size in KB
     * [PARAMS] imageUri - URI of image
     * [PARAMS] context - Application context
     * [RETURNS] File size in KB or -1 on error
     */
    fun getImageSizeKB(imageUri: Uri, context: Context): Long {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val size = inputStream?.available()?.toLong() ?: -1
            inputStream?.close()
            size / 1024  // [CONVERT] Bytes to KB
        } catch (e: Exception) {
            -1
        }
    }
}

/**
 * [USAGE EXAMPLES]
 * 
 * // Anonymous mode - strip all metadata
 * val anonymizedUri = ImageUtils.anonymizeImage(originalUri, context)
 * 
 * // Regular mode - just compress
 * val compressedUri = ImageUtils.compressImage(originalUri, context, maxSizeKB = 1024)
 * 
 * // Check size before upload
 * val sizeKB = ImageUtils.getImageSizeKB(imageUri, context)
 * if (sizeKB > 2048) {
 *     // Compress further
 * }
 */
