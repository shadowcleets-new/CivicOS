import 'package:flutter/material.dart';
import 'package:civicos_app/screens/home_screen.dart';
import 'package:civicos_app/screens/grievance_screen.dart';

void main() {
  runApp(const CivicOSApp());
}

class CivicOSApp extends StatelessWidget {
  const CivicOSApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'CivicOS',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFFFF9933)), # Saffron-ish
        useMaterial3: true,
      ),
      home: const MainWrapper(),
    );
  }
}

class MainWrapper extends StatefulWidget {
  const MainWrapper({super.key});

  @override
  State<MainWrapper> createState() => _MainWrapperState();
}

class _MainWrapperState extends State<MainWrapper> {
  int _selectedIndex = 0;
  
  final List<Widget> _screens = [
    const HomeScreen(),
    const GrievanceScreen(),
  ];

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _showDisclaimer();
    });
  }

  void _showDisclaimer() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (ctx) => AlertDialog(
        title: const Text('⚠️ Liability Disclaimer'),
        content: const Text(
          "This app uses AI to guide you. It is NOT a substitute for legal counsel or emergency services.\n\n"
          "In case of a life-threatening emergency, ALWAYS Dial 112 immediately."
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(),
            child: const Text('I Understand'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _screens[_selectedIndex],
      bottomNavigationBar: NavigationBar(
        selectedIndex: _selectedIndex,
        onDestinationSelected: (idx) => setState(() => _selectedIndex = idx),
        destinations: const [
          NavigationDestination(
            icon: Icon(Icons.emergency_share),
            label: 'Emergency',
          ),
          NavigationDestination(
            icon: Icon(Icons.camera_alt),
            label: 'Grievance',
          ),
        ],
      ),
    );
  }
}
