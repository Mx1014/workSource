//
// EvhSourceVideoConfAccountDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSourceVideoConfAccountDTO
//
@interface EvhSourceVideoConfAccountDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* sourceAccount;

@property(nonatomic, copy) NSString* password;

@property(nonatomic, copy) NSString* confType;

@property(nonatomic, copy) NSNumber* validDate;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* occupyFlag;

@property(nonatomic, copy) NSNumber* occupyAccountId;

@property(nonatomic, copy) NSNumber* confId;

@property(nonatomic, copy) NSString* occupyIdentifierToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

