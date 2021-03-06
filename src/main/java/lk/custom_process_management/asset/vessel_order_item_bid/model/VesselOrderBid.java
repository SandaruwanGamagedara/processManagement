package lk.custom_process_management.asset.vessel_order_item_bid.model;

import lk.custom_process_management.asset.vessel_order_item_bid.entity.VesselOrderItemBid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VesselOrderBid {
  private int id;

  private List< VesselOrderItemBid > vesselOrderItemBids;

}
