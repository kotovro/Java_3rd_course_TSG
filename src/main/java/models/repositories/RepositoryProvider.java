package models.repositories;



import lombok.Getter;
import models.repositories.implementations.inmemory.*;
import models.repositories.interfaces.*;
import models.repositories.sql.*;
import services.ConfigRepository;

@Getter
public class RepositoryProvider {
    private IContractorRepository contractorRepository;
    private ICommentRepository commentRepository;
    private IRequestRepository requestRepository;
    private IResidentRepository residentRepository;
    private IUserRepository userRepository;
    private  IStaffMemberRepository staffMemberRepository;
    private ITimesheetRepository timesheetRepository;
    private IRoleRepository roleRepository;
    private  RepositoryType repositoryType;


    public enum RepositoryType {
        IN_MEMORY,
        IN_DATABASE
    }
    @Getter
    private static RepositoryProvider instance = null;

    public static RepositoryProvider init(ConfigRepository config)
    {
        if (instance == null)
        {
            instance = new RepositoryProvider(config);
        }
        return instance;
    }

    private RepositoryProvider(ConfigRepository config) {
        this.repositoryType = RepositoryType.valueOf(config.getPropertyValue("repository_type"));
        if (repositoryType == RepositoryType.IN_MEMORY)
        {
            this.commentRepository = new CommentRepositoryInMemory();
            this.contractorRepository = new ContractorRepositoryInMemory();
            this.residentRepository = new ResidentRepositoryInMemory();
            this.staffMemberRepository = new StaffMemberRepositoryInMemory();
            this.userRepository = new UserRepositoryInMemory();
            this.timesheetRepository = new TimesheetRepositoryInMemory();
            this.requestRepository = new RequestRepositoryInMemory();
            this.roleRepository = new RoleRepositoryInMemory();
//            this.invoiceRepository = new InvoiceRepositoryInMemory();
        } else if (repositoryType == RepositoryType.IN_DATABASE)
        {
            String url = config.getPropertyValue("db.url");
            String username = config.getPropertyValue("db.username");
            String password = config.getPropertyValue("db.password");
            this.commentRepository = new CommentRepositorySQl(url, username, password);
            this.userRepository = new UserRepositorySQL(url, username, password);
            this.commentRepository = new CommentRepositorySQl(url, username, password);
            this.roleRepository = new RoleRepositorySQL(url, username, password);
            this.requestRepository = new RequestRepositorySQl(url, username, password);
            this.staffMemberRepository = new StaffMemberRepositorySQL(url, username, password);
            this.residentRepository = new ResidentRepositorySQL(url, username, password);
        }
    }


}
