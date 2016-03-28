//
// EvhWebMenuPrivilegeDTO.h
// generated at 2016-03-25 19:05:21 
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

