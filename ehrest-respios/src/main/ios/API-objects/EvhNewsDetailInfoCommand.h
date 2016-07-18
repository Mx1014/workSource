//
// EvhNewsDetailInfoCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsDetailInfoCommand
//
@interface EvhNewsDetailInfoCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* theNewsToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

