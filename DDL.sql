-- Parent: Admins
CREATE TABLE dbo.Admins (
    id NVARCHAR(50) PRIMARY KEY,
    admin_name NVARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender NVARCHAR(10) NOT NULL,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    cnic NVARCHAR(20) NOT NULL,
    contact NVARCHAR(20) NOT NULL,
    email NVARCHAR(100) NOT NULL
);

-- Parent: Accounts
CREATE TABLE dbo.Accounts (
    id NVARCHAR(50) PRIMARY KEY,
    balance DECIMAL(18,2) DEFAULT 0,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password NVARCHAR(300) NOT NULL,
    account_number NVARCHAR(50) UNIQUE NOT NULL,
    account_type NVARCHAR(20) NOT NULL
);

-- Parent: Customers
CREATE TABLE dbo.Customers (
    customer_id INT IDENTITY(1,1) PRIMARY KEY,
    customer_name NVARCHAR(50),
    age INT,
    gender NVARCHAR(20),
    address NVARCHAR(200),
    contact NVARCHAR(50),
    cnic NVARCHAR(100),
    email NVARCHAR(100)
);

-- Junction: CustomerAccounts (references Accounts and Customers)
CREATE TABLE dbo.CustomerAccounts (
    id NVARCHAR(50) NOT NULL,
    customer_id INT NOT NULL,
    CONSTRAINT PK_CustomerAccounts PRIMARY KEY (id, customer_id),
    CONSTRAINT FK_CustomerAccounts_Accounts FOREIGN KEY (id) REFERENCES dbo.Accounts(id),
    CONSTRAINT FK_CustomerAccounts_Customers FOREIGN KEY (customer_id) REFERENCES dbo.Customers(customer_id)
);

-- Parent: Banks
CREATE TABLE dbo.Banks (
    id INT IDENTITY(1,1) PRIMARY KEY,
    bank_name NVARCHAR(100) UNIQUE NOT NULL
);

-- Transactions (child -> Accounts(id))
CREATE TABLE dbo.Transactions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_id NVARCHAR(50) NOT NULL,
    transaction_id NVARCHAR(50) UNIQUE NOT NULL,
    description NVARCHAR(255),
    amount DECIMAL(18,2) NOT NULL,
    transaction_type NVARCHAR(50) NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_time TIME NOT NULL,
    CONSTRAINT FK_Transactions_Accounts FOREIGN KEY (account_id) REFERENCES dbo.Accounts(id)
);

-- Beneficiaries (child -> Accounts(id))
CREATE TABLE dbo.Beneficiaries (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_id NVARCHAR(50) NOT NULL,
    beneficiary_account_number NVARCHAR(20) NOT NULL,
    beneficiary_name NVARCHAR(100),
    beneficiary_bank_name NVARCHAR(100),
    CONSTRAINT FK_Beneficiaries_Accounts FOREIGN KEY (account_id) REFERENCES dbo.Accounts(id)
);

-- Cards (child -> Accounts(account_number))
CREATE TABLE dbo.Cards (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_number NVARCHAR(50) NOT NULL,
    card_holder_name NVARCHAR(100),
    card_number NVARCHAR(20) UNIQUE NOT NULL,
    cvv NVARCHAR(5),
    expiry_date NVARCHAR(10),
    card_type NVARCHAR(10),
    CONSTRAINT FK_Cards_Accounts_account_number FOREIGN KEY (account_number) REFERENCES dbo.Accounts(account_number)
);

-- LoanRequests (child -> Accounts(id))
CREATE TABLE dbo.LoanRequests (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_id NVARCHAR(50) NOT NULL,
    account_holder_name NVARCHAR(100),
    loan_type NVARCHAR(50),
    loan_amount DECIMAL(18,2),
    status NVARCHAR(20) DEFAULT 'Pending',
    CONSTRAINT FK_LoanRequests_Accounts FOREIGN KEY (account_id) REFERENCES dbo.Accounts(id)
);

-- LoanOptions (independent)
CREATE TABLE dbo.LoanOptions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    package_name NVARCHAR(50) UNIQUE NOT NULL,
    loan_amount DECIMAL(18,2) NOT NULL,
    minimum_balance DECIMAL(18,2) NOT NULL,
    duration_months INT NOT NULL,
    interest_percentage DECIMAL(5,2) NOT NULL
);

-- AccountRequests (independent)
CREATE TABLE dbo.AccountRequests (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100),
    age INT,
    gender NVARCHAR(10),
    cnic NVARCHAR(20),
    contact NVARCHAR(20),
    address NVARCHAR(255),
    email NVARCHAR(100),
    account_type NVARCHAR(20),
    status NVARCHAR(20) DEFAULT 'Unanswered',
    request_date DATE DEFAULT GETDATE()
);

-- Feedback (child -> Accounts(id))
CREATE TABLE dbo.Feedback (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_id NVARCHAR(50),
    category_type NVARCHAR(50),
    content NVARCHAR(MAX),
    CONSTRAINT FK_Feedback_Accounts FOREIGN KEY (account_id) REFERENCES dbo.Accounts(id)
);

-- OtherBankAccountHolders (independent)
CREATE TABLE dbo.OtherBankAccountHolders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    bank_name NVARCHAR(100),
    account_number NVARCHAR(50)
);