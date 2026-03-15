import SwiftUI
import PaymentCoreSDK

struct ContentView: View {
    var body: some View {
        ComposeViewController()
            .ignoresSafeArea()
    }
}
struct ComposeViewController: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController()
    }
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}