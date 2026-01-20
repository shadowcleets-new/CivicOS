import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  String _status = "Press for Emergency Assistance";
  bool _isLoading = false;

  Future<void> _handlePanic() async {
    setState(() {
      _isLoading = true;
      _status = "Locating...";
    });

    // Mock Location Logic
    await Future.delayed(const Duration(seconds: 2)); // Simulate API call
    
    // In real app: Position pos = await Geolocator.getCurrentPosition();
    // Then call Backend API: get_authority_by_lat_long(pos.latitude, pos.longitude)
    
    setState(() {
      _isLoading = false;
      _status = "📍 You are in Ward 12, Indiranagar.\n"
                "👮 Nearest Police: Halasuru PS (1.2km)\n"
                "🏥 Nearest Hospital: Manipal (2km)\n\n"
                "Protocol: Golden Hour Active.";
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("CivicOS Crisis Mode")),
      backgroundColor: Colors.red.shade50,
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            GestureDetector(
              onTap: _handlePanic,
              child: Container(
                width: 200,
                height: 200,
                decoration: BoxDecoration(
                  color: Colors.red,
                  shape: BoxShape.circle,
                  boxShadow: [
                    BoxShadow(color: Colors.red.withOpacity(0.5), blurRadius: 20, spreadRadius: 5)
                  ],
                ),
                child: Center(
                  child: _isLoading 
                    ? const CircularProgressIndicator(color: Colors.white)
                    : const Text("SOS", style: TextStyle(color: Colors.white, fontSize: 48, fontWeight: FontWeight.bold)),
                ),
              ),
            ),
            const SizedBox(height: 30),
            Padding(
              padding: const EdgeInsets.all(20.0),
              child: Text(
                _status,
                textAlign: TextAlign.center,
                style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w500),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
