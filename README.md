# Online Ad Platform API

## Description
The API allows you to create ads, group them by category, specify the price and specify the currency.
The system for filtering ads has been implemented.

## Roles
The API has two user roles:
- USER
- ADMIN

## API Reference

### /api/auth
**POST** /login 

`Parameters: {username (REQUIRED), password (REQUIRED)}`

**POST** /registration

`Parameters: {firstname(REQUIRED), lastname(REQUIRED), username(REQUIRED), password(REQUIRED), confirmPassword(REQUIRED)}`

### /api/user

**GET** /getCurrentUser 

**POST** /updateUser

`Parameters: {firstName(OPTIONAL), lastName (OPTIONAL)}`


**POST** {userId}/delete  

`Description: (PERMITTED ONLY FOR ADMIN) Only admin can delete user! But admin cannot delete himself.`

### /api/ads

**POST** /addAdvertisement

`Parameters: {title(REQUIRED), description(REQUIRED), price(REQUIRED), currencyCode(REQUIRED), categoryName(REQUIRED)}`

**GET** /getAllAdvertisements?

                              title={?}  - OPTIONAL
                              &categoryName={?}   - OPTIONAL
                              &currencyCode={?}   - OPTIONAL
                              &forCurrentUser={default = false}  - OPTIONAL
                              &page={default = 0}
                              &size={default = 3}
 
 
**POST** /{advertisementId}/addAttachment

`Parameters: MiltipartFile`

**GET** /{advertisementId}/getAttachments 

`Description: get all attachments by ad ID`


**GET** /file/{fileName}

`Description: Download file by file url`

### /api/categories

**GET** /getAllCategories

**POST** /addCategory 

`Description: PERMITTED ONLY FOR ADMIN`

`Parameters: {categoryName(REQUIRED)}`

### /api/currency

**GET** /getAllCurrency

**POST** /addCurrency

`Description: PERMITTED ONLY FOR ADMIN`

`Parameters: {currencyCode(REQUIRED), currencyName(REQUIRED)}`
