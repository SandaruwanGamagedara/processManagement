package lk.custom_process_management.asset.payment.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusConformation {
  REC("Receiving"),
  PAID("Paid"),
  PEN("Pending");

  private final String statusConformation;

}