//
// EvhListContactGroupNamesByEnterpriseIdCommand.h
// generated at 2016-03-31 20:15:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactGroupNamesByEnterpriseIdCommand
//
@interface EvhListContactGroupNamesByEnterpriseIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

