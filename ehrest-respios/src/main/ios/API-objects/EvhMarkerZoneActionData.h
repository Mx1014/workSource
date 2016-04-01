//
// EvhMarkerZoneActionData.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhMarkerZoneActionData
//
@interface EvhMarkerZoneActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* type;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* categoryId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

