//
// EvhVerifyBuildingAdminCommand.h
// generated at 2016-04-07 15:16:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyBuildingAdminCommand
//
@interface EvhVerifyBuildingAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* buildingId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

