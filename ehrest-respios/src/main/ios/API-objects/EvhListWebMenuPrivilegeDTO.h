//
// EvhListWebMenuPrivilegeDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhWebMenuPrivilegeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWebMenuPrivilegeDTO
//
@interface EvhListWebMenuPrivilegeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* moduleId;

@property(nonatomic, copy) NSString* moduleName;

// item type EvhWebMenuPrivilegeDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

