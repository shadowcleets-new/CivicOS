package com.nivar.app.ui.theme

import androidx.compose.ui.graphics.Color

// ===== Nivar Design System — Color Tokens =====
// Based on Stitch MCP UI/UX Overhaul designs

// Primary Brand — Royal Blue Series
val NivarNavy = Color(0xFF0F2854)   // Darkest Blue — primary, headers, nav
val NivarRoyal = Color(0xFF1C4D8D)  // Medium Blue — secondary, active states
val NivarSky = Color(0xFF4988C4)    // Light Blue — accents, links
val NivarIce = Color(0xFFBDE8F5)    // Pale Blue — chip backgrounds, highlights

// Neutral Surfaces
val OffWhite = Color(0xFFF5F9FA)    // Screen backgrounds
val PureWhite = Color(0xFFFFFFFF)   // Cards, inputs
val NivarSurface = Color(0xFF162032) // Dark mode surface (slightly lighter than Navy)

// Semantic Slate Scale (for text hierarchy)
val NivarSlate50 = Color(0xFFF8FAFC)
val NivarSlate100 = Color(0xFFF1F5F9)  // Light backgrounds, badges
val NivarSlate200 = Color(0xFFE2E8F0)  // Borders, dividers
val NivarSlate300 = Color(0xFFCBD5E1)  // Disabled text, placeholders
val NivarSlate500 = Color(0xFF64748B)  // Secondary text
val NivarSlate700 = Color(0xFF334155)  // Body text (dark theme light)
val NivarSlate800 = Color(0xFF1E293B)  // Primary text
val NivarSlate900 = Color(0xFF0F172A)  // Heading text

// Status Colors
val WarningAmber = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFDC2626)
val SuccessGreen = Color(0xFF16A34A)
val InfoBlue = Color(0xFF3B82F6)

// SOS / Emergency
val SOSRed = Color(0xFFDC2626)
val SOSRedLight = Color(0xFFFFF1F2)  // Emergency screen background

// Semantic Aliases (backward compatibility)
val DeepSlate = NivarNavy
val ElectricTeal = NivarSky

// Category Accent Colors (Ministry tag chips)
val CategoryDefence = Color(0xFFDC2626)      // Red-600
val CategoryHealth = Color(0xFF059669)        // Emerald-600
val CategoryEducation = Color(0xFFD97706)     // Amber-600
val CategoryFinance = Color(0xFF4F46E5)       // Indigo-600
val CategoryLaw = Color(0xFF0D9488)           // Teal-600
val CategoryAgri = Color(0xFF16A34A)          // Green-600
val CategoryDefault = Color(0xFF64748B)       // Slate-500
