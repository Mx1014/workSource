//
// EvhVerifyBuildingAdminCommand.h
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

