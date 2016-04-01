//
// EvhSourceVideoConfAccountDTO.h
// generated at 2016-04-01 15:40:22 
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

