package com.nike.cerberus.hystrix;

import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.model.*;
import com.netflix.hystrix.*;

import java.util.function.Supplier;

/**
 * Hystrix wrapper around AWSKMSClient
 * <p>
 * Most of these commands should execute in their own Thread Pools because they have unique limits.
 * http://docs.aws.amazon.com/kms/latest/developerguide/limits.html#requests-per-second-table
 */
public class HystrixKmsClient extends AWSKMSClient {

    private static final String KMS = "KMS";

    private final AWSKMSClient client;

    public HystrixKmsClient(AWSKMSClient client) {
        this.client = client;
    }

    public EncryptResult encrypt(EncryptRequest request) {
        return execute("KmsEncryptDecrypt", "KmsEncrypt", () -> client.encrypt(request));
    }

    public CreateKeyResult createKey(CreateKeyRequest request) {
        return execute("KmsCreateKey", () -> client.createKey(request));
    }

    public CreateAliasResult createAlias(CreateAliasRequest request) {
        return execute("KmsCreateAlias", () -> client.createAlias(request));
    }

    public DescribeKeyResult describeKey(DescribeKeyRequest request) {
        return execute("KmsDescribeKey", () -> client.describeKey(request));
    }

    public ScheduleKeyDeletionResult scheduleKeyDeletion(ScheduleKeyDeletionRequest request) {
        return execute("KmsScheduleKeyDeletion", () -> client.scheduleKeyDeletion(request));
    }

    public GetKeyPolicyResult getKeyPolicy(GetKeyPolicyRequest request) {
        return execute("KmsGetKeyPolicy", () -> client.getKeyPolicy(request));
    }

    public PutKeyPolicyResult putKeyPolicy(PutKeyPolicyRequest request) {
        return execute("KmsPutKeyPolicy", () -> client.putKeyPolicy(request));
    }

    /**
     * Execute a function that returns a value in a ThreadPool unique to that command.
     */
    private static <T> T execute(String commandKey, Supplier<T> function) {
        return execute(commandKey, commandKey, function);
    }

    /**
     * Execute a function that returns a value in a specified ThreadPool
     */
    private static <T> T execute(String threadPoolName, String commandKey, Supplier<T> function) {
        return new HystrixCommand<T>(buildSetter(threadPoolName, commandKey)) {

            @Override
            protected T run() throws Exception {
                return function.get();
            }
        }.execute();
    }

    private static HystrixCommand.Setter buildSetter(String threadPoolName, String commandKey) {
        return HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(KMS))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadPoolName))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey));
    }

}
