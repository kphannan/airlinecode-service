package com.airline.carrier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * JPA repository definition.
 */
@Repository
// @NoRepositoryBean
public interface AirlineCodeIcaoRepository extends JpaRepository<AirlineCodeIcao, String>
{
}

