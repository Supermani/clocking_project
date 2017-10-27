package com.primeton.manage.employee.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.primeton.manage.employee.entity.Users;

public interface LoginRepository extends JpaRepository<Users, Long> {

	Users findByNameAndPassword(String name, String password);
}
