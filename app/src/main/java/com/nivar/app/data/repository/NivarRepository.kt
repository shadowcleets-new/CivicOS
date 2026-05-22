package com.nivar.app.data.repository

import com.nivar.app.data.model.Grievance
import com.nivar.app.data.model.Scheme
import com.nivar.app.data.remote.ApiService
import com.nivar.app.data.remote.RetrofitClient

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NivarRepository @Inject constructor(private val apiService: ApiService) {
    
    // [FALLBACK] Mock data for when backend is unavailable
    private val mockSchemes = listOf(
        Scheme(1, "PM-KISAN", "Direct income support of ₹6000/year to farmers", "Ministry of Agriculture", "https://pmkisan.gov.in", listOf("farmer", "subsidy", "agriculture")),
        Scheme(2, "Ayushman Bharat (PM-JAY)", "Free health insurance up to ₹5 lakh per family", "Ministry of Health", "https://pmjay.gov.in", listOf("health", "insurance", "poor")),
        Scheme(3, "PM Awas Yojana", "Affordable housing for economically weaker sections", "Ministry of Housing", "https://pmaymis.gov.in", listOf("housing", "subsidy", "poor")),
        Scheme(4, "Sukanya Samriddhi Yojana", "Savings scheme for girl child with tax benefits", "Ministry of Finance", "https://www.nsiindia.gov.in", listOf("girl child", "savings", "education")),
        Scheme(5, "Pradhan Mantri Ujjwala Yojana", "Free LPG connections to BPL families", "Ministry of Petroleum", "https://pmuy.gov.in", listOf("lpg", "bpl", "subsidy")),
        Scheme(6, "MGNREGA", "100 days guaranteed wage employment in rural areas", "Ministry of Rural Development", "https://nrega.nic.in", listOf("employment", "rural", "wage")),
        Scheme(7, "PM Mudra Yojana", "Loans up to ₹10 lakh for micro-enterprises", "Ministry of Finance", "https://www.mudra.org.in", listOf("loan", "business", "msme")),
        Scheme(8, "Atal Pension Yojana","Guaranteed pension for unorganized sector workers", "Ministry of Finance", "https://npscra.nsdl.co.in/atal-pension-yojana.php", listOf("pension", "retirement", "poor")),
        Scheme(9, "Beti Bachao Beti Padhao", "Campaign to save and educate girl children", "Ministry of Women \u0026 Child", "https://wcd.nic.in/bbbp-schemes", listOf("girl child", "education", "awareness")),
        Scheme(10, "PM Kaushal Vikas Yojana", "Skill training for youth to improve employability", "Ministry of Skill Development", "https://www.pmkvyofficial.org", listOf("skill", "training", "youth")),
        Scheme(11, "PM Fasal Bima Yojana", "Crop insurance for farmers against natural calamities", "Ministry of Agriculture", "https://pmfby.gov.in", listOf("farmer", "insurance", "crop")),
        Scheme(12, "Startup India", "Tax exemptions \u0026 funding support for startups", "DPIIT", "https://www.startupindia.gov.in", listOf("startup", "business", "tax")),
        Scheme(13, "Swachh Bharat Mission", "Funding for construction of toilets", "Ministry of Jal Shakti", "https://swachhbharatmission.gov.in", listOf("sanitation", "toilet", "subsidy")),
        Scheme(14, "PM Scholarship Scheme", "Financial assistance to wards of armed forces", "Ministry of Defence", "https://desw.gov.in/prime-ministers-scholarship-scheme", listOf("scholarship", "education", "defence")),
        Scheme(15, "Jal Jeevan Mission", "Tap water connection to every rural household","Ministry of Jal Shakti", "https://jaljeevanmission.gov.in", listOf("water", "rural", "infrastructure"))
    )
    
    suspend fun getSchemes(): Result<List<Scheme>> {
        return try {
            val response = apiService.getSchemes()
            Result.success(response)
        } catch (e: Exception) {
            // [FALLBACK] Return mock data when API fails
            Result.success(mockSchemes)
        }
    }

    suspend fun createGrievance(grievance: Grievance): Result<Grievance> {
        return try {
            val response = apiService.createGrievance(grievance)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
