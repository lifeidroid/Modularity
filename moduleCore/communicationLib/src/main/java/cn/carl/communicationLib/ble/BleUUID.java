package cn.carl.communicationLib.ble;

import java.io.Serializable;
import java.util.UUID;

/**
 * ==============================================
 * author : carl
 * e-mail : 991579741@qq.com
 * time   : 2019/03/11
 * desc   : UUID组
 * version: 1.0
 * ==============================================
 */
public class BleUUID implements Serializable {
    //服务的UUID
    private UUID serviceUUID;
    //特征的UUID
    private UUID characteristicUUID;

    public BleUUID(UUID serviceUUID, UUID characteristicUUID) {
        this.serviceUUID = serviceUUID;
        this.characteristicUUID = characteristicUUID;
    }

    public BleUUID(String serviceUUID, String characteristicUUID) {
        this.serviceUUID = UUID.fromString(serviceUUID);
        this.characteristicUUID = UUID.fromString(characteristicUUID);
    }


    public UUID getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(UUID serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = UUID.fromString(serviceUUID);
    }

    public UUID getCharacteristicUUID() {
        return characteristicUUID;
    }

    public void setCharacteristicUUID(UUID characteristicUUID) {
        this.characteristicUUID = characteristicUUID;
    }
}
