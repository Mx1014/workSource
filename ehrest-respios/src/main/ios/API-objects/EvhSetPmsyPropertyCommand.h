//
// EvhSetPmsyPropertyCommand.h
// generated at 2016-04-30 11:16:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetPmsyPropertyCommand
//
@interface EvhSetPmsyPropertyCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* billTip;

@property(nonatomic, copy) NSString* contact;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

