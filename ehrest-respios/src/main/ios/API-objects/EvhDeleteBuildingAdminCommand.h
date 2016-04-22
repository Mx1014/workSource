//
// EvhDeleteBuildingAdminCommand.h
// generated at 2016-04-22 13:56:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteBuildingAdminCommand
//
@interface EvhDeleteBuildingAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* buildingId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

