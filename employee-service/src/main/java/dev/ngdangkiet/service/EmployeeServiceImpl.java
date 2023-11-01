package dev.ngdangkiet.service;

import com.google.protobuf.Int64Value;
import dev.ngdangkiet.dkmicroservices.common.protobuf.EmptyResponse;
import dev.ngdangkiet.dkmicroservices.employee.protobuf.PEmployee;
import dev.ngdangkiet.dkmicroservices.employee.protobuf.PEmployeeResponse;
import dev.ngdangkiet.dkmicroservices.employee.protobuf.PEmployeesResponse;
import dev.ngdangkiet.dkmicroservices.employee.protobuf.PGetEmployeesRequest;
import dev.ngdangkiet.domain.EmployeeEntity;
import dev.ngdangkiet.domain.PositionEntity;
import dev.ngdangkiet.error.ErrorCode;
import dev.ngdangkiet.mapper.EmployeeMapper;
import dev.ngdangkiet.repository.EmployeeRepository;
import dev.ngdangkiet.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author ngdangkiet
 * @since 10/31/2023
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;

    @Override
    public Int64Value createOrUpdateEmployee(PEmployee pEmployee) {
        long response = ErrorCode.FAILED;
        try {
            if (pEmployee.getId() > 0 && employeeRepository.findById(pEmployee.getId()).isEmpty()) {
                response = ErrorCode.INVALID_DATA;
            }
            EmployeeEntity entity = employeeMapper.toDomain(pEmployee);
            Optional<PositionEntity> position = positionRepository.findById(pEmployee.getPositionId());
            if (position.isPresent()) {
                entity.setPosition(position.get());
                response = employeeRepository.save(entity).getId();
            } else {
                response = ErrorCode.INVALID_DATA;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Int64Value.of(response);
    }

    @Override
    public PEmployeeResponse getEmployeeById(Int64Value employeeId) {
        PEmployeeResponse.Builder builder = PEmployeeResponse.newBuilder()
                .setCode(ErrorCode.FAILED);
        try {
            Optional<EmployeeEntity> entity = employeeRepository.findById(employeeId.getValue());
            if (entity.isPresent()) {
                builder.setCode(ErrorCode.SUCCESS)
                        .setData(employeeMapper.toProtobuf(entity.get()));
            } else {
                builder.setCode(ErrorCode.INVALID_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    @Override
    public PEmployeesResponse getEmployees(PGetEmployeesRequest request) {
        PEmployeesResponse.Builder builder = PEmployeesResponse.newBuilder().setCode(ErrorCode.SUCCESS);
        try {
            List<EmployeeEntity> entities = employeeRepository.findByDepartmentAndPosition(request.getDepartmentId(), request.getPositionId());
            if (!CollectionUtils.isEmpty(entities)) {
                builder.addAllData(employeeMapper.toProtobufs(entities));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.build();
    }

    @Override
    public EmptyResponse deleteEmployeeById(Int64Value request) {
        EmptyResponse.Builder builder = EmptyResponse.newBuilder()
                .setCode(ErrorCode.FAILED);
        try {
            Optional<EmployeeEntity> entity = employeeRepository.findById(request.getValue());
            if (entity.isPresent()) {
                employeeRepository.deleteById(request.getValue());
                builder.setCode(ErrorCode.SUCCESS);
            } else {
                builder.setCode(ErrorCode.INVALID_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.build();
    }
}
