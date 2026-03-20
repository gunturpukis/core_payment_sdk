import SwiftUI
//#if canImport(PaymentGatewaySDK)
import PaymentGatewaySDK
//#endif

struct ContentView: View {
    var body: some View {
        ComposeViewController()
            
    }
}

struct ComposeViewController: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        return makePaymentViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
// MARK: - SDK Bridge
private func makePaymentViewController() -> UIViewController {
//    #if canImport(PaymentGatewaySDK)
    // TODO: Replace with the correct factory/initializer from PaymentCoreSDK.
    // The PaymentCoreSDK did not expose a usable Swift API for payment view controller creation.
    // Update this once the correct API is known.
    let sdk = PaymentSDK.Companion.shared
    let instance = sdk.shared()
    let vc = instance.createViewController()
    
    return vc
//    return PlaceholderPaymentViewController(
//        message: "PaymentCoreSDK linked, but creation API is unknown. Replace factory call in makePaymentViewController()."
//    )
//    #else
    // SDK not available at build time; show a friendly placeholder so the app still runs.
//    return PlaceholderPaymentViewController(
//        message: "PaymentCoreSDK is not available. Ensure the SDK is added to the target and imported."
//    )
//    #endif
}

// MARK: - Placeholder VC shown when SDK isn't available or factory is unknown
private final class PlaceholderPaymentViewController: UIViewController {
    private let message: String

    init(message: String) {
        self.message = message
        super.init(nibName: nil, bundle: nil)
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) { fatalError("init(coder:) has not been implemented") }

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .systemBackground

        let label = UILabel()
        label.text = message
        label.textAlignment = .center
        label.textColor = .secondaryLabel
        label.numberOfLines = 0
        label.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(label)
        NSLayoutConstraint.activate([
            label.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            label.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            label.leadingAnchor.constraint(greaterThanOrEqualTo: view.leadingAnchor, constant: 20),
            label.trailingAnchor.constraint(lessThanOrEqualTo: view.trailingAnchor, constant: -20)
        ])
    }
}

