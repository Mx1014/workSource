//
// EvhGetUserOpPromotionCommand.h
// generated at 2016-03-28 15:56:09 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserOpPromotionCommand
//
@interface EvhGetUserOpPromotionCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

