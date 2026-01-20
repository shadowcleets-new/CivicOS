import SwiftUI

struct ServicesView: View {
    var body: some View {
        NavigationView {
            List {
                Section(header: Text("Legal Drafting")) {
                    NavigationLink(destination: Text("RTI Form")) {
                        ServiceRow(title: "Draft RTI Application", subtitle: "Get info from dept")
                    }
                    NavigationLink(destination: Text("Gas Form")) {
                        ServiceRow(title: "Gas Connection Transfer", subtitle: "Move Indane/HP gap")
                    }
                }
                
                Section(header: Text("Welfare Schemes")) {
                    NavigationLink(destination: Text("Finder")) {
                        ServiceRow(title: "Check Eligibility", subtitle: "Find schemes")
                    }
                    NavigationLink(destination: Text("Health")) {
                        ServiceRow(title: "Ayushman Bharat", subtitle: "Health Insurance")
                    }
                }
            }
            .navigationTitle("Gov Services")
        }
    }
}

struct ServiceRow: View {
    let title: String
    let subtitle: String
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(title).font(.headline)
            Text(subtitle).font(.caption).foregroundColor(.gray)
        }
    }
}
