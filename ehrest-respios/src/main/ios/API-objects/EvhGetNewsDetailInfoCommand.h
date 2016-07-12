//
// EvhGetNewsDetailInfoCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetNewsDetailInfoCommand
//
@interface EvhGetNewsDetailInfoCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* theNewsToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

