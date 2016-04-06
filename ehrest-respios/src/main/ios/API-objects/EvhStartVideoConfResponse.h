//
// EvhStartVideoConfResponse.h
// generated at 2016-04-06 19:59:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhStartVideoConfResponse
//
@interface EvhStartVideoConfResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* confHostId;

@property(nonatomic, copy) NSString* confHostName;

@property(nonatomic, copy) NSNumber* maxCount;

@property(nonatomic, copy) NSString* token;

@property(nonatomic, copy) NSString* meetingNo;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

