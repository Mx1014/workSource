//
// EvhBizConfHolder.h
// generated at 2016-04-12 15:02:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhObject.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBizConfHolder
//
@interface EvhBizConfHolder
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) id data;

@property(nonatomic, copy) NSNumber* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

