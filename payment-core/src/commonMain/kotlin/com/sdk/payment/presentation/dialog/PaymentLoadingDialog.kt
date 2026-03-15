//package com.sdk.payment.presentation.dialog
//
//@Composable
//fun PaymentLoadingDialog(
//    show: Boolean
//) {
//    if (!show) return
//
//    Dialog(onDismissRequest = { }) {
//
//        Card(
//            shape = RoundedCornerShape(24.dp),
//            elevation = CardDefaults.cardElevation(8.dp),
//            modifier = Modifier
//                .width(300.dp)
//        ) {
//
//            Column(
//                modifier = Modifier
//                    .padding(32.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                CircularProgressIndicator(
//                    modifier = Modifier.size(80.dp),
//                    strokeWidth = 8.dp,
//                    color = Color(0xFF1565C0)
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Text(
//                    text = "Processing Your Payment",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = "Please wait a moment while we complete your transaction.",
//                    fontSize = 14.sp,
//                    color = Color.Gray,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//    }
//}