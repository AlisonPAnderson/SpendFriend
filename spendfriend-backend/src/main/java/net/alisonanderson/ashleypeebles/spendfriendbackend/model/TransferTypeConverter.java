package net.alisonanderson.ashleypeebles.spendfriendbackend.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class TransferTypeConverter implements AttributeConverter<TransferType, String> {


    @Override
    public String convertToDatabaseColumn(TransferType transferType) {
        if(transferType == null) {
            return null;
        }
        return transferType.getTransferTypeDescription();
    }

    @Override
    public TransferType convertToEntityAttribute(String s) {
        return null;
    }
}
