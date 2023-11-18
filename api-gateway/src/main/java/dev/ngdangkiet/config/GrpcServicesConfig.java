package dev.ngdangkiet.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import dev.ngdangkiet.constant.ServiceConstant;
import dev.ngdangkiet.dkmicroservices.auth.service.AuthServiceGrpc;
import dev.ngdangkiet.dkmicroservices.department.service.DepartmentServiceGrpc;
import dev.ngdangkiet.dkmicroservices.employee.service.EmployeeServiceGrpc;
import dev.ngdangkiet.dkmicroservices.notification.service.NotificationServiceGrpc;
import dev.ngdangkiet.grpc.BaseGrpcServicesConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ngdangkiet
 * @since 10/31/2023
 */

@Configuration
public class GrpcServicesConfig extends BaseGrpcServicesConfig {

    public GrpcServicesConfig(EurekaClient eurekaClient) {
        super(eurekaClient);
    }

    @Bean
    public DepartmentServiceGrpc.DepartmentServiceBlockingStub departmentServiceBlockingStub() {
        InstanceInfo instanceInfo = getGrpcInstanceInfo(ServiceConstant.DEPARTMENT_SERVICE);
        return DepartmentServiceGrpc.newBlockingStub(newChannel(instanceInfo.getHostName(), instanceInfo.getPort()));
    }

    @Bean
    public EmployeeServiceGrpc.EmployeeServiceBlockingStub employeeServiceBlockingStub() {
        InstanceInfo instanceInfo = getGrpcInstanceInfo(ServiceConstant.EMPLOYEE_SERVICE);
        return EmployeeServiceGrpc.newBlockingStub(newChannel(instanceInfo.getHostName(), instanceInfo.getPort()));
    }

    @Bean
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub() {
        InstanceInfo instanceInfo = getGrpcInstanceInfo(ServiceConstant.AUTH_SERVICE);
        return AuthServiceGrpc.newBlockingStub(newChannel(instanceInfo.getHostName(), instanceInfo.getPort()));
    }

    @Bean
    public NotificationServiceGrpc.NotificationServiceBlockingStub notificationServiceBlockingStub() {
        InstanceInfo instanceInfo = getGrpcInstanceInfo(ServiceConstant.NOTIFICATION_SERVICE);
        return NotificationServiceGrpc.newBlockingStub(newChannel(instanceInfo.getHostName(), instanceInfo.getPort()));
    }
}
