package models.repositories;



import lombok.Getter;
import models.repositories.implementations.inmemory.*;
import models.repositories.interfaces.*;

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
    private IPermissionsRepository permissionRepository;
    private RepositoryType repositoryType;

    public enum RepositoryType {
        IN_MEMORY;
    }
    @Getter
    private static RepositoryProvider instance = null;

    public static RepositoryProvider init(RepositoryType type)
    {
        if (instance == null)
        {
            instance = new RepositoryProvider(type);
        }
        else if (type != RepositoryType.IN_MEMORY)
        {
            throw new RuntimeException("RepositoryProvider is already inited with another Type");
        }
        return instance;
    }

    private RepositoryProvider(RepositoryType type) {
        this.repositoryType = type;
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
            this.permissionRepository = new PermissionRepositoryInMemory();
//            this.invoiceRepository = new InvoiceRepositoryInMemory();

        }
    }


}
