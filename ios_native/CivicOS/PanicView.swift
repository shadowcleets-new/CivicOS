import SwiftUI

struct PanicView: View {
    @State private var isLocating = false
    @State private var statusText = "Press for Emergency Assistance"
    
    var body: some View {
        ZStack {
            Color(red: 1.0, green: 0.9, blue: 0.9) // Red-ish
                .ignoresSafeArea()
            
            VStack(spacing: 40) {
                Button(action: handlePanic) {
                    ZStack {
                        Circle()
                            .fill(Color.red)
                            .frame(width: 200, height: 200)
                            .shadow(color: .red.opacity(0.5), radius: 20, x: 0, y: 10)
                        
                        if isLocating {
                            ProgressView()
                                .progressViewStyle(CircularProgressViewStyle(tint: .white))
                                .scaleEffect(2)
                        } else {
                            Text("SOS")
                                .font(.system(size: 48, weight: .bold))
                                .foregroundColor(.white)
                        }
                    }
                }
                
                Text(statusText)
                    .font(.headline)
                    .multilineTextAlignment(.center)
                    .padding()
            }
        }
    }
    
    func handlePanic() {
        isLocating = true
        statusText = "Locating..."
        
        // Mock Async delay
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            isLocating = false
            statusText = """
            📍 Ward 12, Indiranagar
            👮 Nearest Police: Halasuru PS (1.2km)
            🏥 Nearest Hospital: Manipal (2km)
            
            Protocol: Golden Hour Active.
            """
        }
    }
}
