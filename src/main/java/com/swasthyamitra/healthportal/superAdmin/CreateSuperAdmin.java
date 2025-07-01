package com.swasthyamitra.healthportal.superAdmin;

import com.swasthyamitra.healthportal.entity.UserInfoEntity;
import com.swasthyamitra.healthportal.enums.RoleEnum;
import com.swasthyamitra.healthportal.repository.UserInfoRepository;
import com.swasthyamitra.healthportal.utils.CommonUtils;
import com.swasthyamitra.healthportal.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class CreateSuperAdmin implements CommandLineRunner {

    private final UserInfoRepository userInfoRepository;
    @Value("${super.admin.firstName}")
    private String firstName;
    @Value("${super.admin.lastName}")
    private String lastName;
    @Value("${super.admin.email}")
    private String email;

    @Autowired
    public CreateSuperAdmin(UserInfoRepository userInfoRepository
                          ) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public void run(String... args) throws ParseException {
        Optional<UserInfoEntity> userInfoEntity = userInfoRepository.findByEmailAndIsDeletedFalse(email);


        if (userInfoEntity.isEmpty()) {
            String password = CommonUtils.generateDummyPassword();
            UserInfoEntity userInfo = new UserInfoEntity();
            userInfo.setFirstName(firstName);
            userInfo.setLastName(lastName);
            userInfo.setEmail(email);
            userInfo.setPassword(password);
            System.out.println(password);

            // 🔧 Set required non-null fields
            Date dob = DateUtils.stringToDate("01-01-1990");
            userInfo.setDateOfBirth(dob);
            userInfo.setPhoneNumber("9999999999");
            userInfo.setGender("Male");
            userInfo.setPinCode("110001");
            userInfo.setAddress("System Admin HQ");
            userInfo.setMaritalStatus("Single");

            userInfo.setEncodedPassword(CommonUtils.hashPassword(password));
            userInfo.setRoleEnum(RoleEnum.SUPER_ADMIN);
            userInfoRepository.save(userInfo);
        }
    }


}


