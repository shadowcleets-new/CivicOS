import 'package:flutter/material.dart';
// import 'package:camera/camera.dart'; 

class GrievanceScreen extends StatelessWidget {
  const GrievanceScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Grievance Cam")),
      body: Column(
        children: [
          Expanded(
            child: Container(
              color: Colors.black,
              child: const Center(
                child: Text("Camera Preview Here", style: TextStyle(color: Colors.white)),
              ),
            ),
          ),
          Container(
            padding: const EdgeInsets.all(20),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                IconButton(
                  onPressed: () {}, 
                  icon: const Icon(Icons.camera, size: 50, color: Colors.blue)
                ),
                const Text("AI Analyzing: Pothole Detected..."),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
