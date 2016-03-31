//
// EvhWebMenuPrivilegeDTO.h
// generated at 2016-03-31 11:07:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWebMenuPrivilegeDTO
//
@interface EvhWebMenuPrivilegeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* menuId;

@property(nonatomic, copy) NSNumber* privilegeId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* showFlag;

@property(nonatomic, copy) NSString* discription;

@property(nonatomic, copy) NSNumber* sortNum;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

