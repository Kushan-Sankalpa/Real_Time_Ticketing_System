package org.example.server.Repository;


import org.example.server.Entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
<<<<<<< HEAD
    //
}
=======
    Vendor findByEmail(String email);
}
>>>>>>> 01450f842c042e9d4f9b6c56835f9b566a897e8e
