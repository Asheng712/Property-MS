package com.wisdom.service.impl;

import com.wisdom.context.BaseContext;
import com.wisdom.dto.UpdateProfileDTO;
import com.wisdom.dto.UserLoginDTO;
import com.wisdom.dto.UserRegisterDTO;
import com.wisdom.entity.Contract;
import com.wisdom.entity.House;
import com.wisdom.entity.Role;
import com.wisdom.entity.User;
import com.wisdom.exception.BusinessException;
import com.wisdom.mapper.AssetMapper;
import com.wisdom.mapper.ComplaintMapper;
import com.wisdom.mapper.ContractMapper;
import com.wisdom.mapper.RoleMapper;
import com.wisdom.mapper.UserMapper;
import com.wisdom.service.UserService;
import com.wisdom.util.JwtTokenUtil;
import com.wisdom.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ComplaintMapper complaintMapper;

    @Autowired
    private RoleMapper roleMapper;

    public UserServiceImpl(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        User user = userMapper.selectByUsername(userLoginDTO.getUsername());
        if (user == null) {
            throw BusinessException.notFound("USER_NOT_FOUND");
        }
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw BusinessException.badRequest("PASSWORD_ERROR");
        }
        return jwtTokenUtil.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        User existingUser = userMapper.selectByUsername(userRegisterDTO.getUsername());
        if (existingUser != null) {
            throw BusinessException.badRequest("USERNAME_ALREADY_EXISTS");
        }
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    public UserVO getCurrentUserInfo() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw BusinessException.unauthorized();
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("USER_NOT_FOUND");
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setRealName(user.getRealName());
        userVO.setPhone(user.getPhone());
        userVO.setEmail(user.getEmail());
        userVO.setAvatar(user.getAvatar());
        userVO.setStatus(user.getStatus());
        userVO.setRoleId(user.getRoleId());
        if (user.getRoleId() != null) {
            Role role = roleMapper.selectById(user.getRoleId());
            userVO.setRoleName(role != null ? role.getRoleName() : "未知角色");
        } else {
            userVO.setRoleName("未分配角色");
        }
        return userVO;
    }

    @Override
    public void updateProfile(UpdateProfileDTO updateProfileDTO) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw BusinessException.unauthorized();
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.notFound("USER_NOT_FOUND");
        }

        boolean nameChanged = updateProfileDTO.getRealName() != null
                && !updateProfileDTO.getRealName().equals(user.getRealName());
        boolean phoneChanged = updateProfileDTO.getPhone() != null
                && !updateProfileDTO.getPhone().equals(user.getPhone());

        if (updateProfileDTO.getRealName() != null) {
            user.setRealName(updateProfileDTO.getRealName());
        }
        if (updateProfileDTO.getPhone() != null) {
            user.setPhone(updateProfileDTO.getPhone());
        }
        if (updateProfileDTO.getEmail() != null) {
            user.setEmail(updateProfileDTO.getEmail());
        }
        if (updateProfileDTO.getAvatar() != null) {
            user.setAvatar(updateProfileDTO.getAvatar());
        }
        userMapper.updateById(user);

        // 同步更新名下的房产信息和合同租户名
        if (nameChanged || phoneChanged) {
            List<House> houses = assetMapper.selectHousesByOwnerId(userId);
            for (House house : houses) {
                if (nameChanged) {
                    house.setOwnerName(updateProfileDTO.getRealName());
                }
                if (phoneChanged) {
                    house.setOwnerPhone(updateProfileDTO.getPhone());
                }
                assetMapper.updateById(house);

                // 同步该房产关联的合同租户名
                if (nameChanged) {
                    List<Contract> contracts = contractMapper.selectByHouseId(house.getId());
                    for (Contract contract : contracts) {
                        contract.setTenantName(updateProfileDTO.getRealName());
                        contractMapper.updateById(contract);
                    }
                }
            }

            // 同步更新投诉记录中的来源字段
            if (nameChanged) {
                complaintMapper.updateSourceByReporterId(userId, updateProfileDTO.getRealName());
            }
        }
    }

    @Override
    public boolean isCurrentUserAdmin() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) return false;
        User user = userMapper.selectById(userId);
        return user != null && user.getRoleId() != null && user.getRoleId() == 1L;
    }

    @Override
    public Long getRequiredCurrentUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw BusinessException.unauthorized();
        }
        return userId;
    }

    @Override
    public List<Long> getOwnedHouseIds(Long userId) {
        List<House> houses = assetMapper.selectHousesByOwnerId(userId);
        return houses.stream().map(House::getId).collect(Collectors.toList());
    }
}