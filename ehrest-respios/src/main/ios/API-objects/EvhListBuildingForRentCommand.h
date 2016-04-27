//
// EvhListBuildingForRentCommand.h
// generated at 2016-04-26 18:22:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBuildingForRentCommand
//
@interface EvhListBuildingForRentCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* buildingId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

