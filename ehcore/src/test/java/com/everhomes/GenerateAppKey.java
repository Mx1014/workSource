package com.everhomes.generate;

import java.util.UUID;

import com.everhomes.util.SignatureHelper;

public class GenerateAppKey {
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
		System.out.println(SignatureHelper.generateSecretKey());
	}
}
