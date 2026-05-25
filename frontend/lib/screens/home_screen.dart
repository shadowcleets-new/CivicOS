import 'package:flutter/material.dart';

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
            Semantics(
              button: true,
              label: 'Trigger Emergency SOS',
              child: Material(
                color: Colors.red,
                shape: const CircleBorder(),
                elevation: 10,
                shadowColor: Colors.red.withValues(alpha: 0.5),
                child: InkWell(
                  onTap: _isLoading ? null : _handlePanic,
                  customBorder: const CircleBorder(),
                  child: SizedBox(
                    width: 200,
                    height: 200,
                    child: Center(
                      child: _isLoading
                        ? const CircularProgressIndicator(color: Colors.white)
                        : const Text("SOS", style: TextStyle(color: Colors.white, fontSize: 48, fontWeight: FontWeight.bold)),
                    ),
                  ),
                ),
              ),
            ),
            const SizedBox(height: 30),
            Padding(
              padding: const EdgeInsets.all(20.0),
              child: Semantics(
                liveRegion: true,
                child: Text(
                  _status,
                  textAlign: TextAlign.center,
                  style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w500),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
