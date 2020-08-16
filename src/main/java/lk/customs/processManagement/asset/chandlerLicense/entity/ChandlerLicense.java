package lk.customs.processManagement.asset.chandlerLicense.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.customs.processManagement.asset.chandler.entity.Chandler;
import lk.customs.processManagement.asset.chandlerLicense.entity.Enum.LicenseStatus;
import lk.customs.processManagement.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "ChandlerLicense" )
public class ChandlerLicense extends AuditEntity {

    @Column(unique = true)
    private String number;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Enumerated( EnumType.STRING)
    private LicenseStatus licenseStatus;

    @ManyToOne
    private Chandler chandler;


}
