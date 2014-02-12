package org.mobicents.media.core.ice.network.stun.message.attributes.address;

import org.mobicents.media.core.ice.network.stun.message.attributes.StunAttribute;

/**
 * The XOR-RELAYED-ADDRESS attribute is given by a TURN server to indicates the
 * client its relayed address.
 * 
 * It has the same format as XOR-MAPPED-ADDRESS.
 */
public class XorRelayedAddressAttribute extends XorMappedAddressAttribute {
	public static final String NAME = "XOR-RELAYED-ADDRESS";

	protected XorRelayedAddressAttribute() {
		super(StunAttribute.XOR_RELAYED_ADDRESS);
	}

	public String getName() {
		return NAME;
	}
}