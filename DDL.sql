/*
CREATE TABLE Admins (
    id NVARCHAR(50) PRIMARY KEY,
    admin_Name NVARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender NVARCHAR(10) NOT NULL,
    username NVARCHAR(50) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    cnic NVARCHAR(20) NOT NULL,
    contact NVARCHAR(20) NOT NULL,
    email NVARCHAR(100) NOT NULL
);
*/

/*
CREATE TABLE AccountHolders (
    id NVARCHAR(50) PRIMARY KEY,
    account_holder_name NVARCHAR(100) NOT NULL,
    balance DECIMAL(18,2) DEFAULT 0,
    age INT,
    gender NVARCHAR(10),
    username NVARCHAR(50) UNIQUE NOT NULL,
    password NVARCHAR(300) NOT NULL,
    account_number NVARCHAR(50) UNIQUE NOT NULL,
    account_type NVARCHAR(20) NOT NULL,
    address NVARCHAR(300),
    contact NVARCHAR(50) NOT NULL,
    cnic NVARCHAR(50) NOT NULL,
    email NVARCHAR(100) NOT NULL
);
*/

/*
CREATE TABLE Banks (
    id INT IDENTITY(1,1) PRIMARY KEY,
    bank_name NVARCHAR(100) UNIQUE NOT NULL
);
*/

/*
CREATE TABLE Transactions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_id NVARCHAR(50) NOT NULL,
    transaction_id NVARCHAR(50) UNIQUE NOT NULL,
    description NVARCHAR(255),
    amount DECIMAL(18,2) NOT NULL,
    transaction_type NVARCHAR(50) NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_time TIME NOT NULL,
    FOREIGN KEY (account_id) REFERENCES AccountHolders(id)
);
*/

/*
CREATE TABLE Beneficiaries (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_id NVARCHAR(50) NOT NULL,
    beneficiary_account_number NVARCHAR(20) NOT NULL,
    beneficiary_name NVARCHAR(100),
    beneficiary_bank_name NVARCHAR(100),
    FOREIGN KEY (account_id) REFERENCES AccountHolders(id)
);
*/

/*
CREATE TABLE Cards (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_number NVARCHAR(50) NOT NULL,
    card_holder_name NVARCHAR(100),
    card_number NVARCHAR(20) UNIQUE NOT NULL,
    cvv NVARCHAR(5),
    expiry_date NVARCHAR(10),
    card_type NVARCHAR(10),
    FOREIGN KEY (account_number) REFERENCES AccountHolders(account_number)
);
*/

/*
CREATE TABLE LoanRequests (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_id NVARCHAR(50) NOT NULL,
    account_holder_name NVARCHAR(100),
    loan_type NVARCHAR(50),
    loan_amount DECIMAL(18,2),
    status NVARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (account_id) REFERENCES AccountHolders(id)
);
*/

/*
CREATE TABLE AccountRequests (
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
*/

/*
CREATE TABLE Feedback (
    id INT IDENTITY(1,1) PRIMARY KEY,
    account_id NVARCHAR(50),
    category_type NVARCHAR(50),
    content NVARCHAR(MAX),
    FOREIGN KEY (account_id) REFERENCES AccountHolders(id)
);
*/

/*
CREATE TABLE OtherBankAccountHolders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    bank_name NVARCHAR(100),
    account_number NVARCHAR(50)
);
*/
