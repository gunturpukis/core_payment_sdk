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
private func makePaymentViewController() -> UIViewController {
    #if canImport(PaymentGatewaySDK)

    let paymentRequest = PaymentRequest(
        externalId: "14rRAb1eiS",
        orderId: "OKo2H5B8gm",
        currency: "IDR",
        source: "payment_page",
        paymentMethod: "card",
        paymentChannel: "BRICC",
        paymentMode: "CLOSE",
        paymentDetails: PaymentDetails(
            amount: 10000,
            isCustomerPayingFee: false,
            transactionDescription: "Clothes",
            expiredTime: ""
        ),
        itemDetails: [
            ItemDetails(
                itemId: "Artikel 1",
                name: "shirt",
                amount: 10000,
                qty: 1,
                description: "3131"
            )
        ],
        customerDetails: CustomerDetails(
            email: "solutions@ifortepay.id",
            fullName: "Testing",
            phone: "08970799128",
            ipAddress: "182.30.91.67"
        ),
        billingAddress: BillingAddres(
            fullName: "CC Test",
            phone: "0893456789",
            address: "Kosan Hj Hasan",
            city: "Tangerang",
            postalCode: "19127",
            country: "ID"
        ),
        shippingAddress: ShippingAddres(
            fullName: "MCP",
            phone: "0893456789",
            address: "Bandara Mas",
            city: "Malang",
            postalCode: "10210",
            country: "ID"
        ),
        cardDetails: CardDetails(
            cardNumber: "",
            cardExpiredMonth: "",
            cardExpiredYear: "",
            cardCvn: "",
            cardHolderName: ""
        ),
        returnUrl: "https://superapp-stg.ifortepay.id/",
        callbackUrl: "https://mcpid.free.beeceptor.com",
       
       
        paymentOptions: PaymentOptions(
            useRewards: true,
            campaign_code: "002",
            tenor: "0"
        ),
        additionalData: "",
    )
        

    let credential = "MC2026016183:0x85e19e9ff024614509:U07P9kpkmYeUDLiqZZVymciPnr3QdN/tL+XBL3Adkck"
    let token = Data(credential.utf8).base64EncodedString()
    let json = JsonHelper().toJson(request: paymentRequest)
    print(json)
    

    return MainViewControllerKt.MainViewController(token: token, json: json)

    #else
    return PlaceholderPaymentViewController(
        message: "SDK not available"
    )
    #endif
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
