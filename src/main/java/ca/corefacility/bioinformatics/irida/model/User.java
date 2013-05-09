package ca.corefacility.bioinformatics.irida.model;

import ca.corefacility.bioinformatics.irida.model.alibaba.UserIF;
import ca.corefacility.bioinformatics.irida.model.roles.impl.Audit;
import ca.corefacility.bioinformatics.irida.model.roles.Auditable;
import ca.corefacility.bioinformatics.irida.model.roles.Identifiable;
import ca.corefacility.bioinformatics.irida.model.roles.impl.UserIdentifier;
import ca.corefacility.bioinformatics.irida.validators.Patterns;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 * A user object.
 *
 * @author Franklin Bristow <franklin.bristow@phac-aspc.gc.ca>
 * @author Thomas Matthews <thomas.matthews@phac-aspc.gc.ca>
 */
public class User implements UserIF, Comparable<User>{

    private UserIdentifier id;
    @NotNull(message = "{user.username.notnull}")
    @Size(min = 3, message = "{user.username.size}")
    private String username;
    @NotNull(message = "{user.email.notnull}")
    @Size(min = 5, message = "{user.email.size}")
    @Email(message = "{user.email.invalid}")
    private String email;
    @NotNull(message = "{user.password.notnull}")
    @Size(min = 6, message = "{user.password.size}") // passwords must be at least six characters long
    @Patterns({
        @Pattern(regexp = "^.*[A-Z].*$", message = "{user.password.uppercase}"), // passwords must contain an upper-case letter
        @Pattern(regexp = "^.*[0-9].*$", message = "{user.password.number}") // passwords must contain a number
    })
    private String password;
    @NotNull(message = "{user.firstName.notnull}")
    @Size(min = 2, message = "{user.firstName.size}")
    private String firstName;
    @NotNull(message = "{user.lastName.notnull}")
    @Size(min = 2, message = "{user.lastName.size}")
    private String lastName;
    @NotNull(message = "{user.phoneNumber.notnull}")
    @Size(min = 4, message = "{user.phoneNumber.size}")
    private String phoneNumber;
    @NotNull
    private Audit audit;

    public User() {
        audit = new Audit();
    }

    public User(String username, String email, String password, String firstName, String lastName, String phoneNumber) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public User(UserIdentifier id, String username, String email, String password, String firstName, String lastName, String phoneNumber) {
        this(username, email, password, firstName, lastName, phoneNumber);
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, firstName, lastName, phoneNumber);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User u = (User) other;
            return Objects.equals(id, u.id)
                    && Objects.equals(username, u.username)
                    && Objects.equals(email, u.email)
                    && Objects.equals(password, u.password)
                    && Objects.equals(firstName, u.firstName)
                    && Objects.equals(lastName, u.lastName)
                    && Objects.equals(phoneNumber, u.phoneNumber);
        }

        return false;
    }

    @Override
    public int compareTo(User u) {
        return audit.compareTo(u.audit);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(User.class)
                .add("username", username)
                .add("email", email)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("phoneNumber", phoneNumber)
                .toString();
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Audit getAuditInformation() {
        return audit;
    }

    @Override
    public UserIdentifier getIdentifier() {
        return id;
    }

    @Override
    public void setIdentifier(UserIdentifier identifier) {
        this.id = identifier;
    }

    @Override
    public void setAuditInformation(Audit audit) {
        this.audit = audit;
    }
}
