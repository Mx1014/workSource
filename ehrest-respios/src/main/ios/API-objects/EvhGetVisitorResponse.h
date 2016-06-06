//
// EvhGetVisitorResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetVisitorResponse
//
@interface EvhGetVisitorResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* doorName;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* qr;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

