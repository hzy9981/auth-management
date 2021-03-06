package com.shz.project.domain.system.user;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shz.foundation.utils.Identities;
import com.shz.foundation.utils.validate.EmailValidater;
import com.shz.foundation.utils.validate.PhoneNumberValidater;
import com.shz.foundation.utils.validate.UsernameValidater;
import com.shz.project.domain.system.EncryptService;
import com.shz.project.domain.system.department.Department;
import com.shz.project.domain.system.role.Role;

@Service
@Transactional
public class SystemUserManager {
	
	@Autowired
	private SystemUserRepository repository;
	@Autowired
	private ValidateEmailSender emailSender;

	/**
	 * 新增用户时，检查用户名是否重复，为密码进行加密
	 * @param entity
	 * @return
	 */
	public SystemUser add(SystemUser entity) {
		if(entity==null)return null;
		
		//用户名检查
		String username=entity.getUsername();
		checkUsername(username);
		//邮箱检查
		String email=entity.getEmail();
		checkEmail(email);
		
		//手机号码检查
		String telephone=entity.getTelephone();
		checkTelephone(telephone);
		
		//生成加密密码
		String password=entity.getPassword();
		checkPassword(password);
		
		String salt=EncryptService.generateSalt();
		entity.setSalt(salt);
		password=EncryptService.encryptPassword(password, salt);
		entity.setPassword(password);
		entity=repository.save(entity);
		return entity;
	}
	
	public SystemUser update(String id, String username, String realname, Boolean verified, String email, String telephone,
			Department department, Set<Role> roles) {
		SystemUser user=repository.findOne(id);
		if(user==null) return null;
		if(!user.getUsername().equals(username)) {
			checkUsername(username);
		}
		if(!user.getEmail().equals(email)) {
			checkEmail(email);
		}
		if(!user.getTelephone().equals(telephone)) {
			checkTelephone(telephone);
		}
		user.setUsername(username);
		user.setRealname(realname);
		user.setVerified(verified);
		user.setEmail(email);
		user.setTelephone(telephone);
		user.setDepartment(department);
		user.setRoles(roles);
		return user;
	}
	
	public SystemUser regist(String username, String password, String realname, String email, String telephone, Department department) {
		SystemUser user=new SystemUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setRealname(realname);
		user.setPassword(password);
		user.setEmail(email);
		user.setTelephone(telephone);
		user.setDepartment(department);
		user.setVerified(false);
		return add(user);
	}
	
	/**修改密码，需要用户记得原密码*/
	public SystemUser changePassword(SystemUser user, String oldPassword, String newPassword) {
		checkPassword(newPassword);
		
		String salt=user.getSalt();
		String originPassword=user.getPassword();
		if(!EncryptService.rightPassword(oldPassword, salt, originPassword)) {
			throw new RuntimeException("您输入的原密码不正确");
		}
		
		salt=EncryptService.generateSalt();
		user.setSalt(salt);
		String password=EncryptService.encryptPassword(newPassword, salt);
		user.setPassword(password);
		return user;
	}
	
	public SystemUser checkEmailValidater(String emailValidater) throws RuntimeException {
		SystemUser user=repository.getByEmailValidater(emailValidater);
		if(user==null) {
			throw new RuntimeException("邮件凭据不存在");
		}
		return user;
	}
	
	/**通过发送到邮箱的验证凭据，重置密码*/
	public SystemUser changePasswordByEmail(String emailValidater, String password) {
		SystemUser user=checkEmailValidater(emailValidater);
		checkPassword(password);
		String salt=EncryptService.generateSalt();
		user.setSalt(salt);
		password=EncryptService.encryptPassword(password, salt);
		user.setPassword(password);
		user.setEmailValidater(null);
		return user;
	}
	
	/**向指定邮箱发送验证邮件*/
	public void emailValidate(String email) {
		SystemUser user=repository.getByEmail(email);
		if(user==null) throw new RuntimeException("邮箱名不存在");
		String emailValidater=Identities.uuid2();
		user.setEmailValidater(emailValidater);
		emailSender.sendMail(email, emailValidater);
	}
	
	/**检查用户名，需要满足2~10位数字+字母这个规则，不能和已有用户名重复，不能有特殊符号*/
	private void checkUsername(String username) {
		if(StringUtils.isBlank(username)) throw new RuntimeException("用户名不能为空");
		if(!UsernameValidater.isUsername(username)) {
			throw new RuntimeException("只能使用2~10位字母或数字组成的用户名");
		}
		//用户名检查
		SystemUser user=repository.getByUsername(username);
		if(user!=null) {
			throw new RuntimeException("用户名已存在，请更换");
		}
		
	}
	
	/**检查邮箱，需要满足邮箱地址格式，不能重复*/
	private void checkEmail(String email) {
		//邮箱检查
		if(StringUtils.isBlank(email)) {
			throw new RuntimeException("需要填写邮箱地址");
		}
		if(!EmailValidater.isEmail(email)) {
			throw new RuntimeException("请填写正确的邮箱地址");
		}
		SystemUser eUser=repository.getByEmail(email);
		if(eUser!=null) {
			throw new RuntimeException("邮箱地址已存在，请更换");
		}
	}
	
	/**检查手机号，需要满足11位手机格式，不能有重复*/
	private void checkTelephone(String telephone) {
		//手机号码检查
		if(StringUtils.isBlank(telephone)) {
			throw new RuntimeException("需要填写手机号码");
		}
		if(!PhoneNumberValidater.isMobile(telephone)) {
			throw new RuntimeException("请填写正确的11位手机号码");
		}
		SystemUser tUser=repository.getByEmail(telephone);
		if(tUser!=null) {
			throw new RuntimeException("手机号码已存在，请更换");
		}
	}
	private void checkPassword(String password) {
		if(StringUtils.isBlank(password)) {
			throw new RuntimeException("密码不能为空");
		}
		if(password.length()<6) {
			throw new RuntimeException("密码长度不能小于6位");
		}
	}
}
