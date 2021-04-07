package lk.custom_process_management.asset.payment.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusConformation {
  INR("Item Not Receiving"),
  REC("Receiving"),
  PAIDSHIPAGENT("Paid Ship Agent"),
  RECEVINGPAYMENT("Receiving Payment"),
  ORDERCOLOSE("Order Close"),
  PEN("Pending");

  private final String statusConformation;

}
