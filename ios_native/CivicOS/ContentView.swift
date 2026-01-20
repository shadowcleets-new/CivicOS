import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {
            PanicView()
                .tabItem {
                    Label("Emergency", systemImage: "light.beacon.max")
                }
            
            GrievanceView()
                .tabItem {
                    Label("Grievance", systemImage: "camera")
                }
            
            ServicesView()
                .tabItem {
                    Label("Services", systemImage: "doc.text")
                }
        }
    }
}
