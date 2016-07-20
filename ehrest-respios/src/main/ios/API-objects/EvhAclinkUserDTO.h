//
// EvhAclinkUserDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUserDTO
//
@interface EvhAclinkUserDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSNumber* authId;

@property(nonatomic, copy) NSNumber* rightOpen;

@property(nonatomic, copy) NSNumber* rightVisitor;

@property(nonatomic, copy) NSNumber* rightRemote;

@property(nonatomic, copy) NSNumber* companyId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSNumber* buildingId;

@property(nonatomic, copy) NSString* company;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

