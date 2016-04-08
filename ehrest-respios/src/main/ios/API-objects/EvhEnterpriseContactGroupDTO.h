//
// EvhEnterpriseContactGroupDTO.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseContactGroupDTO
//
@interface EvhEnterpriseContactGroupDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* applyGroup;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* parentGroupName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

