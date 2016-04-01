//
// EvhApplaunchAppActionData.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApplaunchAppActionData
//
@interface EvhApplaunchAppActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* pkg;

@property(nonatomic, copy) NSString* download;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

